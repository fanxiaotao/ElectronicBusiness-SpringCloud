package com.grc.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.grc.common.IDUtils;
import com.grc.item.domain.*;
import com.grc.item.mapper.ItemMapper;
import com.grc.item.service.ItemService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 郭若辰
 * @create 2017-03-01 16:12
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemMapper itemMapper;

    //访问图片的url
    @Value("${application.access_base_url}")
    private String accessBaseUrl;

    //图片上传的根路径
    @Value("${application.files.localpath}")
    private String basePath;

    /*
    根据id查询商品
     */
    @Override
    public Item getItemById(Long itemId) {
        return itemMapper.getItemById(itemId);
    }

    /*
    查询商品列表
     */
    @Override
    public List<Item> getItemsPageable(int page, int rows) {
        PageHelper.startPage(page, rows);//分页处理
        return itemMapper.getItemsPageable();
    }

    /*
    选择商品类目
     */
    @Override
    public List<ItemCategory> getItemCategroy(Long parentId) {
        return itemMapper.getItemCategroy(parentId);
    }

    /*
    上传图片
     */
    @Override
    public String upload(MultipartFile uploadFile) throws IOException {
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        //取原始图片名(比如test.jpg)
        String oldname = uploadFile.getOriginalFilename();
        //生成新图片名(随机字符串+原始图片名的后缀)
        String newName = IDUtils.genImageName() + oldname.substring(oldname.lastIndexOf("."));
        //图片上传的相对路径
        String relPath = File.separator + "itemPics" + File.separator + date + File.separator;
        //图片上传
        FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), new File(basePath + relPath, newName));
        //访问图片的相对路径
        String relUrl = "/" + "itemPics" + "/" + date + "/" + newName;
        //返回访问图片的路径
        return accessBaseUrl + relUrl;
    }

    /*
    新增商品
     */
    @Override
    public Long insertItem(Item item, String desc, String itemParams) {
        /*
        * 生成商品id
        * 不使用MySQL的自增主键
        */
        Long id = IDUtils.genItemId();
        item.setId(id);
        itemMapper.insertItem(item);
        //创建商品描述对象
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemMapper.insertItemDesc(itemDesc);
        //创建商品规格参数信息对象
        ItemParamMsg itemParamMsg = new ItemParamMsg();
        itemParamMsg.setItemId(id);
        itemParamMsg.setParamData(itemParams);
        itemMapper.insertItemParamMsg(itemParamMsg);
        return id;
    }

    /*
    查询商品规格参数模板列表
     */
    @Override
    public List<Map<String, Object>> getParamsPageable(int page, int rows) {
        PageHelper.startPage(page, rows);
        return itemMapper.getParamsPageable();
    }

    /*
    删除规格参数模板
     */
    @Override
    public void deleteParams(List<Long> ids) {
        for (Long id : ids) {
            itemMapper.deleteParams(id);
        }
    }

    /*
    判断选择的类目是否已经添加过规格模板
     */
    @Override
    public Long queryItemCatId(Long catId) {
        return itemMapper.queryItemCatId(catId);

    }

    /*
    获取规格模板
     */
    @Override
    public String getExistParam(Long catId) {
        return itemMapper.getExistParam(catId);
    }

    /*
    新增商品类目的规格模板
     */
    @Override
    public void insertParam(ItemParam itemParam) {
        itemMapper.insertParam(itemParam);
    }

    /*
    获取商品描述信息
     */
    @Override
    public ItemDesc loadItemDesc(Long itemId) {
        return itemMapper.loadItemDesc(itemId);
    }

    /*
    获取商品的规格参数信息
     */
    @Override
    public ItemParam loadItemParam(Long itemId) {
        return itemMapper.loadItemParam(itemId);
    }

    /*
    删除商品
    */
    @Override
    public void deleteItems(List<Long> ids) {
        for (Long id : ids) {
            itemMapper.deleteItem(id);
            itemMapper.deleteItemDesc(id);
            itemMapper.deleteItemParamMsg(id);
        }
    }

    /*
    下架商品
     */
    @Override
    public void instockItem(List<Long> ids) {
        for (Long id : ids) {
            itemMapper.instockItem(id);
        }
    }

    /*
    上架商品
     */
    @Override
    public void reshelfItem(List<Long> ids) {
        for (Long id : ids) {
            itemMapper.reshelfItem(id);
        }
    }

    /*
    编辑商品
     */
    @Override
    public void updateItem(Item item, String desc, Long itemParamId, String itemParams) {
        itemMapper.updateItem(item);
        //创建商品描述对象
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemMapper.updateItemDesc(itemDesc);
        //创建商品规格参数信息对象
        ItemParamMsg itemParamMsg = new ItemParamMsg();
        itemParamMsg.setId(itemParamId);
        itemParamMsg.setParamData(itemParams);
        itemMapper.updateItemParamMsg(itemParamMsg);
    }
}
